package com.example.bp.analysis;

import com.example.bp.analysis.model.Circle;
import com.example.bp.dal.entity.IrisFinding;
import com.example.bp.dal.entity.IrisMap;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IrisComputerVisionEngine {
    public List<IrisFinding> analyse(String imagePath, IrisMap map) {
        Mat source = Imgcodecs.imread(imagePath);
        Mat gray = new Mat();
        Imgproc.cvtColor(source, gray, Imgproc.COLOR_BGR2GRAY);

        Circle pupil = findPupil(gray);

        Mat normalisedStrip = unwrap(gray, pupil);

        return detectArtifacts(normalisedStrip, map);
    }

    private Circle findPupil(Mat gray) {
        Mat circles = new Mat();

        Imgproc.medianBlur(gray, gray, 5);

        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0, (double)gray.rows() / 8,
        100, 30, 20, 100);

        if (circles.cols() > 0) {
            double[] data = circles.get(0, 0);
            return new Circle(new Point(data[0], data[1]), (int)data[2]);
        }
        throw new RuntimeException("Could not locate the pupil in the image");
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
}
