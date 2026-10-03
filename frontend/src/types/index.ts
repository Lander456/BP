// Common types
export type Sex = 'MALE' | 'FEMALE';

// ==========================================
// Authentication DTOs
// ==========================================

export interface LoginRequestDto {
    username: string;
    password: string;
}

export interface AuthResponseDto {
    token: string;
    user: IridologistDetailDto;
}

// ==========================================
// Iridologist DTOs
// ==========================================

export interface IridologistCreateDto {
    username: string;
    firstName: string;
    lastName: string;
    password: string;
}

export interface IridologistDetailDto {
    id: number;
    firstName: string;
    lastName: string;
    username: string;
}

export interface IridologistListDto {
    firstName: string;
    lastName: string;
}

export interface IridologistUpdateDto {
    firstName?: string | null;
    lastName?: string | null;
    password?: string | null;
}

// ==========================================
// Patient DTOs
// ==========================================

export interface PatientCreateDto {
    firstName: string;
    lastName: string;
    age?: number | null;
    birthNum?: string | null;
    sex?: Sex | null;
}

export interface PatientDetailDto {
    id: number;
    firstName: string;
    lastName: string;
    age?: number | null;
    birthNum?: string | null;
    sex?: Sex | null;
}

export interface PatientListDto {
    id: number;
    age?: number | null;
    firstName: string;
    lastName: string;
}

export interface PatientUpdateDto {
    iridologistId?: number | null;
    firstName?: string | null;
    lastName?: string | null;
    sex?: Sex | null;
    birthNum?: string | null;
}

// ==========================================
// Artifact DTOs
// ==========================================

export interface ArtifactCreateDto {
    name: string;
    description?: string | null;
    labelCode: string;
}

export interface ArtifactDetailDto {
    id: number;
    name: string;
    description?: string | null;
}

export interface ArtifactListDto {
    id: number;
    name: string;
}

export interface ArtifactUpdateDto {
    id: number;
    name?: string | null;
    description?: string | null;
}

// ==========================================
// Iris Image DTOs
// ==========================================

export interface IrisImageCreateDto {
    patientId: number;
    eyeSide?: string | null;
    notes?: string | null;
}

export interface IrisImageDetailDto {
    id: number;
    imageUrl: string;
    originalFileName: string;
    contentType: string;
    fileSize: number;
    patientFirstName: string;
    patientLastName: string;
    patientAge?: number | null;
    patientSex?: Sex | null;
}

export interface IrisImageListDto {
    id: number;
    imageUrl: string;
    uploadedAt: string;
    label?: string | null;
}

export interface IrisImageUpdateDto {
    eyeSide?: string | null;
    notes?: string | null;
}

// ==========================================
// Iris Sector DTOs
// ==========================================

export interface IrisSectorCreateDto {
    irisMapId: number;
    name: string;
    startAngle: number;
    endAngle: number;
    innerRadius?: number | null;
    outerRadius?: number | null;
    description?: string | null;
}

export interface IrisSectorDetailDto {
    id: number;
    name: string;
    startAngle: number;
    endAngle: number;
    innerRadius?: number | null;
    outerRadius?: number | null;
    description?: string | null;
    irisMapId: number;
}

export interface IrisSectorListDto {
    id: number;
    name: string;
    startAngle: number;
    endAngle: number;
}

export interface IrisSectorUpdateDto {
    id: number;
    name?: string | null;
    startAngle?: number | null;
    endAngle?: number | null;
    innerRadius?: number | null;
    outerRadius?: number | null;
    description?: string | null;
}

// ==========================================
// Iris Map DTOs
// ==========================================

export interface IrisMapCreateDto {
    imageUrl: string;
    iridologistId: number;
    sectors?: IrisSectorCreateDto[] | null;
}

export interface IrisMapDetailDto {
    id: number;
    imageUrl: string;
    iridologistId: number;
    sectors: IrisSectorDetailDto[];
}

export interface IrisMapListDto {
    id: number;
    imageUrl: string;
    iridologistId: number;
    sectorCount: number;
}

export interface IrisMapUpdateDto {
    id: number;
    imageUrl?: string | null;
    sectors?: IrisSectorCreateDto[] | null;
}

// ==========================================
// Iris Finding DTOs
// ==========================================

export interface IrisFindingCreateDto {
    irisImageId: number;
    irisSectorId: number;
    artifactId: number;
    confidenceScore?: number | null;
    geometryJson: string;
}

export interface IrisFindingDetailDto {
    id: number;
    irisImageImageId: string;
    sector: IrisSectorDetailDto;
    artifact: ArtifactDetailDto;
    geometryJson: string;
    confidenceScore?: number | null;
    isGenerated?: boolean;
    isValidated?: boolean;
}

export interface IrisFindingListDto {
    id: number;
    artifactName: string;
    sectorName: string;
    confidenceScore?: number | null;
    isGenerated?: boolean;
    isValidated?: boolean;
}

export interface IrisFindingUpdateDto {
    id: number;
    irisSectorId?: number | null;
    artifactId?: number | null;
    geometryJson?: string | null;
    isValidated?: boolean | null;
}
