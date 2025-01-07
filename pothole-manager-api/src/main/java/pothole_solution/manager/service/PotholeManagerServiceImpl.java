package pothole_solution.manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pothole_solution.core.domain.pothole.dto.PotFltPotMngrServDto;
import pothole_solution.core.domain.pothole.dto.request.ReqPotChgPrgsStusPotMngrServDto;
import pothole_solution.core.domain.pothole.dto.request.ReqPotRegPotMngrServDto;
import pothole_solution.core.domain.pothole.entity.*;
import pothole_solution.core.domain.pothole.repository.PotholeHistoryImageRepository;
import pothole_solution.core.domain.pothole.repository.PotholeHistoryRepository;
import pothole_solution.core.domain.pothole.repository.PotholeQueryDslRepository;
import pothole_solution.core.domain.pothole.repository.PotholeRepository;
import pothole_solution.core.domain.pothole.service.RoadAddressSearchService;
import pothole_solution.core.infra.s3.ImageService;

import java.util.List;

import static pothole_solution.core.global.exception.CustomException.NONE_POTHOLE;

@Service
@Transactional
@RequiredArgsConstructor
public class PotholeManagerServiceImpl implements PotholeManagerService {
    private final PotholeRepository potholeRepository;
    private final PotholeHistoryRepository potholeHistoryRepository;
    private final PotholeHistoryImageRepository potholeHistoryImageRepository;
    private final PotholeQueryDslRepository potholeQueryDslRepository;
    private final ImageService imageService;
    private final RoadAddressSearchService roadAddressSearchService;

    @Override
    public Pothole registerPothole(ReqPotRegPotMngrServDto reqPotRegPotMngrServDto, List<MultipartFile> registerPotholeImages) {
        // Pothole 저장
        Pothole pothole = reqPotRegPotMngrServDto.toPothole();

        RoadAddress roadAddress = roadAddressSearchService.getRoadAddress(reqPotRegPotMngrServDto.getLon() + "," + reqPotRegPotMngrServDto.getLat());
        pothole.initAddress(roadAddress.getText(), roadAddress.getStructure().getLevel4L(), roadAddress.getZipcode(), roadAddress.getStructure().getLevel4LC());
        potholeRepository.save(pothole);

        // 포트홀 등록 이미지 S3에 업로드 및 썸네일 설정
        String dirName = uploadDirName(pothole.getPotholeId(), pothole.getProcessStatus().getValue());
        List<String> imageUrls = imageService.uploadImages(registerPotholeImages, dirName);
        pothole.createThumbnail(imageUrls.get(0));

        // PotholeHistory 생성
        PotholeHistory potholeHistory = new PotholeHistory(pothole, pothole.getProcessStatus());
        potholeHistoryRepository.save(potholeHistory);

        // PotholeHistoryImage 생성
        for (String imageUrl : imageUrls) {
            PotholeHistoryImage potholeHistoryImage = new PotholeHistoryImage(potholeHistory, imageUrl);
            potholeHistoryImageRepository.save(potholeHistoryImage);
        }

        return pothole;
    }

    @Override
    public Pothole registerPothole(ReqPotRegPotMngrServDto reqPotRegPotMngrServDto, String videoLink) {
        // Pothole 저장
        Pothole pothole = reqPotRegPotMngrServDto.toPothole();

        RoadAddress roadAddress = roadAddressSearchService.getRoadAddress(reqPotRegPotMngrServDto.getLon() + "," + reqPotRegPotMngrServDto.getLat());
        pothole.initAddress(roadAddress.getText(), roadAddress.getStructure().getLevel4L(), roadAddress.getZipcode(), roadAddress.getStructure().getLevel4LC());
        potholeRepository.save(pothole);

        // TODO: 포트홀 영상에서 대표 이미지 추출 및 저장

        // PotholeHistory 생성
        PotholeHistory potholeHistory = new PotholeHistory(pothole, pothole.getProcessStatus());
        potholeHistoryRepository.save(potholeHistory);

        // TODO: PotholeHistoryImage 생성

        return pothole;
    }

    private String uploadDirName(Long potholeId, String progressStatus) {
        return potholeId + "/" + progressStatus;
    }

    @Override
    @Transactional(readOnly = true)
    public Pothole getPotholeWithPotholeHistoryByPotholeId(Long potholeId) {
        return potholeRepository.findPotholeWithPotholeHistoryByPotholeId(potholeId)
                                .orElseThrow(
                                        () -> NONE_POTHOLE
                                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pothole> getAllPotholes() {
        return potholeRepository.findAll();
    }

    @Override
    public Pothole changePotholeProgressStatus(Long potholeId, ReqPotChgPrgsStusPotMngrServDto changePotholeProcessStatusRequestDto) {
        Pothole pothole = potholeRepository.findPotholeWithPotholeHistoryByPotholeId(potholeId)
                                           .orElseThrow(
                                                   () -> NONE_POTHOLE
                                           );

        pothole.changeProgress(changePotholeProcessStatusRequestDto.getProgressStatus());

        // 해당 진행 상태에 대한 PotholeHistory 가 없다면 생성
        createPotholeHistoryToProgressStatus(pothole);

        return pothole;
    }

    private void createPotholeHistoryToProgressStatus(Pothole pothole) {
        boolean isProcessStatusIncluded = pothole.getPotholeHistories().stream()
                                                           .anyMatch(potholeHistory ->
                                                                   potholeHistory.getProcessStatus().equals(pothole.getProcessStatus())
                                                           );

        if (!isProcessStatusIncluded) {
            PotholeHistory potholeHistory = new PotholeHistory(pothole, pothole.getProcessStatus());
            potholeHistoryRepository.save(potholeHistory);
        }
    }

    @Override
    public void deletePothole(Long potholeId) {
        Pothole pothole = findByPotholeId(potholeId);

        String thumbnail = pothole.getThumbnail();

        // PotholeHistoryImage
        List<PotholeHistoryImage> potholeHistoryImages = potholeHistoryImageRepository.findAllByPotholeId(potholeId);

        List<Long> potholeHistoryImageIds = potholeHistoryImages.stream()
                                                                .map(PotholeHistoryImage::getPotholeHistoryImgId)
                                                                .toList();

        List<String> potholeHistoryImageUrls = potholeHistoryImages.stream()
                                                                   .map(PotholeHistoryImage::getImage)
                                                                   .toList();

        // PotholeHistoryImage 삭제
        potholeHistoryImageRepository.deleteAllByIds(potholeHistoryImageIds);

        // PotholeHistory 삭제
        potholeHistoryRepository.deleteAllByPotholeId(potholeId);

        // Pothole 삭제
        potholeRepository.deleteById(potholeId);

        // S3 PotholeHistoryImage 삭제
        if (!potholeHistoryImageUrls.isEmpty()) {
            potholeHistoryImageUrls.forEach(imageService::deleteImage);
        }

        // S3 Pothole Thumbnail 삭제
        if (thumbnail != null && !thumbnail.isEmpty()) {
            imageService.deleteImage(thumbnail);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pothole> getFilteredPotholes(PotFltPotMngrServDto potFltPotMngrServDto) {
        Integer availableMinImportance = (potFltPotMngrServDto.getMinImportance() == null) ? 0 : potFltPotMngrServDto.getMinImportance();
        Integer availableMaxImportance = (potFltPotMngrServDto.getMaxImportance() == null) ? 100 : potFltPotMngrServDto.getMaxImportance();

        potFltPotMngrServDto.changeToAvailableImportance(availableMinImportance, availableMaxImportance);

        return potholeQueryDslRepository.findByFilter(potFltPotMngrServDto);
    }

    private Pothole findByPotholeId(Long potholeId) {
        return potholeRepository.findById(potholeId)
                                .orElseThrow(
                                        () -> NONE_POTHOLE
                                );
    }
}
