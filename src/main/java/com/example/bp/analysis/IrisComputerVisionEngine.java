package com.example.bp.analysis;

import com.example.bp.analysis.model.Circle;
import com.example.bp.dal.entity.IrisFinding;
import com.example.bp.dal.entity.IrisMap;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.photo.Photo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IrisComputerVisionEngine {
    public void previewImage(String path) {
        Mat source = Imgcodecs.imread(path);
        if (source.empty()) {
            System.err.println("Error: could not load image at: " + path);
            return;
        }

        Mat gray = new Mat();
        Imgproc.cvtColor(source, gray, Imgproc.COLOR_BGR2GRAY);

        try {
            Circle pupil = findPupil(gray);

            Circle irisBoundary = findIrisBoundary(gray, pupil);

            Mat normalizedStrip = unwrap(gray, pupil);

            Mat detectionMap = source.clone();

            Imgproc.circle(detectionMap, pupil.center(), (int) pupil.radius(), new org.opencv.core.Scalar(0, 255, 0), 3);
            Imgproc.circle(detectionMap, pupil.center(), (int) irisBoundary.radius(), new org.opencv.core.Scalar(0, 0, 255), 3);

            String detWin = "1. Pupil Detection";
            HighGui.namedWindow(detWin, HighGui.WINDOW_NORMAL);
            HighGui.resizeWindow(detWin, 800, 600);
            HighGui.imshow(detWin, detectionMap);

            String stripWin = "2. Unwrapped Iris (Normalized)";
            HighGui.namedWindow(stripWin, HighGui.WINDOW_NORMAL);
            HighGui.resizeWindow(stripWin, 1000, 300);
            HighGui.imshow(stripWin, normalizedStrip);

            System.out.println("Windows open. Press any key in a window to close and exit.");
            HighGui.waitKey(0);

        } catch (Exception e) {
            System.err.println("Analysis failed: " + e.getMessage());
            HighGui.namedWindow("failed", HighGui.WINDOW_NORMAL);
            HighGui.resizeWindow("failed", 1000, 300);
            HighGui.imshow("failed", source);
            HighGui.waitKey(0);
        } finally {
            HighGui.destroyAllWindows();
        }
    }

    public List<IrisFinding> analyse(String imagePath, IrisMap map) {
        Mat source = Imgcodecs.imread(imagePath);
        Mat gray = new Mat();
        Imgproc.cvtColor(source, gray, Imgproc.COLOR_BGR2GRAY);

        Circle pupil = findPupil(gray);

        Mat normalisedStrip = unwrap(gray, pupil);

        return detectArtifacts(normalisedStrip, map);
    }

    private Circle findPupil(Mat gray) {

        Mat workingCopy = new Mat();

        gray.copyTo(workingCopy);

        Point roughCentre = findRoughCentre(gray);

        int boxSize = (int) (workingCopy.cols()*0.6);

        int startX = (int) Math.max(0, roughCentre.x - boxSize / 2.0);
        int startY = (int) Math.max(0, roughCentre.y - boxSize / 2.0);
        int actualWidth = Math.min(boxSize, workingCopy.cols() - startX);
        int actualHeight = Math.min(boxSize, workingCopy.rows() - startY);

        Rect roi = new Rect(startX, startY, actualWidth, actualHeight);
        Mat eyeROI = new Mat(workingCopy, roi);
        removeGlint(eyeROI);

        Point initialCenter = new Point(eyeROI.cols()/2.0, eyeROI.rows()/2.0);
        int wobbleRange = eyeROI.cols() / 10;
        int step = 2;

        int minR = (int) (eyeROI.cols() * 0.2);
        int maxR = (int) (eyeROI.cols() * 0.6);

        double bestX = initialCenter.x;
        double bestY = initialCenter.y;
        double bestR = 0;
        double maxJump = -1;

        for (int ox = -wobbleRange; ox <= wobbleRange; ox += step) {
            for (int oy = -wobbleRange; oy <= wobbleRange; oy += step) {
                Point currentCenter = new Point(initialCenter.x + ox, initialCenter.y + oy);

                for (int r = minR; r < maxR; r++) {
                    double val1 = getCircleAverage(eyeROI, currentCenter, r);
                    double val2 = getCircleAverage(eyeROI, currentCenter, r + 2);

                    if (val1 > 65) {
                        break;
                    }

                    double jump = val2 - val1;
                    double weightedJump = jump * ((255 - val1) / 255.0);

                    if (weightedJump > maxJump) {
                        maxJump = weightedJump;
                        bestR = r;
                        bestX = currentCenter.x;
                        bestY = currentCenter.y;
                    }
                }
            }
        }

        Point finalCenter = new Point(bestX, bestY);

        Imgproc.circle(eyeROI, finalCenter, (int)bestR, new Scalar(255), 2);

        debugShowImg(eyeROI, "PUPIL FOUND");

        Mat filtered = new Mat();
        Imgproc.bilateralFilter(workingCopy, filtered, 9, 75, 75);
        filtered.copyTo(workingCopy);
        filtered.release();

        Point actualCenter = new Point(roi.x + finalCenter.x, roi.y + finalCenter.y);

        return new Circle(actualCenter, bestR);
    }

    private double getCircleAverage(Mat img, Point center, double r) {
        double sum = 0;
        int points = 80;

        for (int i = 0; i < points; i++) {
            double theta = (2 * Math.PI * i) / points;
            int x = (int)(center.x + r * Math.cos(theta));
            int y = (int)(center.y + r * Math.sin(theta));

            if (x >= 0 && x < img.cols() && y >=0 && y < img.rows()) {
                sum += img.get(y, x)[0];
            }
        }
        return sum / points;
    }

    private Point findRoughCentre(Mat gray) {
        Mat small = new Mat();
        Imgproc.resize(gray, small, new Size(gray.cols()/4.0, gray.rows()/4.0));

        Imgproc.threshold(small, small, 50, 255, Imgproc.THRESH_BINARY_INV);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
        Imgproc.erode(small, small, kernel);

        Moments m = Imgproc.moments(small);
        if (m.m00 != 0) {
            int x = (int) (m.m10 / m.m00) * 4;
            int y = (int) (m.m01 / m.m00) * 4;
            return new Point(x, y);
        }

        return new Point(gray.cols()/2.0, gray.rows()/2.0);
    }

    private void removeGlint(Mat eyeROI) {
        Mat mask = new Mat();
        Imgproc.threshold(eyeROI, mask, 160, 255, Imgproc.THRESH_BINARY);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(15, 15));
        Imgproc.dilate(mask, mask, kernel);

        Photo.inpaint(eyeROI, mask, eyeROI, 30, Photo.INPAINT_TELEA);

        mask.release();
        kernel.release();
    }

    private @NonNull Mat unwrap(@NonNull Mat gray, @NonNull Circle pupil) {
        int irisRadius = (int) (pupil.radius() * 2.5);

        Mat out = new Mat((int) (irisRadius - pupil.radius()), 360, gray.type());

        Imgproc.warpPolar(gray, out, out.size(), pupil.center(), irisRadius, Imgproc.WARP_POLAR_LINEAR);

        return out;
    }

    private List<IrisFinding> detectArtifacts(Mat normalisedStrip, IrisMap map) {
        return null;
    }

    private void debugShowImg(Mat img, String winName) {
        HighGui.namedWindow(winName, HighGui.WINDOW_NORMAL);
        HighGui.resizeWindow(winName, 1000, 300);
        HighGui.imshow(winName, img);
        HighGui.waitKey(0);
    }

    private Circle findIrisBoundary(Mat gray, Circle pupil) {
        int wobble = 20;
        double maxJump = -1;

        Circle bestIris = new Circle(pupil.center(), pupil.radius() * 2.5);

        for (int ox = -wobble; ox <= wobble; ox += 4) {
            for (int oy = -wobble; oy <= wobble; oy +=4) {
                Point testCenter = new Point(pupil.center().x + ox, pupil.center().y + oy);

                for (int r = (int)(pupil.radius() * 1.5); r < (int)(pupil.radius() * 4.0); r += 2) {
                    double v1 = getCircleAverage(gray, testCenter, r);
                    double v2 = getCircleAverage(gray, testCenter, r + 5);

                    double jump = v2 - v1;
                    if (jump > maxJump) {
                        maxJump = jump;
                        bestIris = new Circle(testCenter, r);
                    }
                }
            }
        }
        return bestIris;
    }
}
