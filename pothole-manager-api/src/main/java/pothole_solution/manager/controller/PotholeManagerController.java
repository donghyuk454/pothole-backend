package pothole_solution.manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pothole_solution.core.domain.pothole.dto.PotFltPotMngrServDto;
import pothole_solution.core.domain.pothole.dto.request.ReqPotChgPrgsStusPotMngrServDto;
import pothole_solution.core.domain.pothole.dto.request.ReqPotRegPotMngrServDto;
import pothole_solution.core.domain.pothole.dto.response.RespPotDetsInfoPotMngrCntrDto;
import pothole_solution.core.domain.pothole.dto.response.RespPotHistGetPotMngrCntrDto;
import pothole_solution.core.domain.pothole.dto.response.RespPotSimInfoPotMngrCntrDto;
import pothole_solution.core.domain.pothole.entity.Pothole;
import pothole_solution.core.domain.pothole.entity.PotholeHistory;
import pothole_solution.core.domain.pothole.entity.PotholeHistoryImage;
import pothole_solution.core.domain.pothole.entity.Progress;
import pothole_solution.core.global.util.response.BaseResponse;
import pothole_solution.manager.service.PotholeHistoryImageManagerService;
import pothole_solution.manager.service.PotholeManagerService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pothole/v1/manager")
public class PotholeManagerController {
    private final PotholeManagerService potholeManagerService;
    private final PotholeHistoryImageManagerService potholeHistoryImageManagerService;

    @PostMapping
    public BaseResponse<RespPotSimInfoPotMngrCntrDto> registerPothole(@Valid @RequestPart(value = "registerPothole") ReqPotRegPotMngrServDto reqPotRegPotMngrServDto,
                                                                      @RequestPart(value = "registerPotholeImages") List<MultipartFile> registerPotholeImages){

        Pothole pothole = potholeManagerService.registerPothole(reqPotRegPotMngrServDto.toPothole(), registerPotholeImages);

        return new BaseResponse<>(new RespPotSimInfoPotMngrCntrDto(pothole));
    }

    @GetMapping("/{potholeId}")
    public BaseResponse<RespPotDetsInfoPotMngrCntrDto> getPotholeDetails(@PathVariable("potholeId") Long potholeId) {
        Pothole pothole = potholeManagerService.getPotholeWithPotholeHistoryByPotholeId(potholeId);
        List<PotholeHistory> potholeHistories = pothole.getPotholeHistories();
        List<PotholeHistoryImage> potholeHistoryImages = potholeHistoryImageManagerService.getAllPotholeHistoryImageByPotholeHistoryId(potholeHistories);

        return new BaseResponse<>(new RespPotDetsInfoPotMngrCntrDto(pothole, getPotholeHistoryResponseDtoList(potholeHistories, potholeHistoryImages)));
    }

    private List<RespPotHistGetPotMngrCntrDto> getPotholeHistoryResponseDtoList(List<PotholeHistory> potholeHistories, List<PotholeHistoryImage> potholeHistoryImages) {
        return potholeHistories.stream()
                                .map(potholeHistory -> new RespPotHistGetPotMngrCntrDto(
                                        potholeHistory,
                                        getPotholeHistoryImagesByPotholeHistory(potholeHistoryImages, potholeHistory.getPotholeHistoryId())
                                ))
                                .toList();
    }

    private List<PotholeHistoryImage> getPotholeHistoryImagesByPotholeHistory(List<PotholeHistoryImage> potholeHistoryImages, Long potholeHistoryId) {
        return potholeHistoryImages.stream()
                                   .filter(potholeHistoryImage -> potholeHistoryImage.getPotholeHistory().getPotholeHistoryId().equals(potholeHistoryId))
                                   .toList();
    }

    @GetMapping("/potholes")
    public BaseResponse<List<RespPotSimInfoPotMngrCntrDto>> getAllPotholes() {
        List<Pothole> potholes = potholeManagerService.getAllPotholes();

        return new BaseResponse<>(potholes.stream()
                                          .map(RespPotSimInfoPotMngrCntrDto::new)
                                          .toList()
        );
    }

    @PutMapping("/{potholeId}")
    public BaseResponse<RespPotSimInfoPotMngrCntrDto> changePotholeProgressStatus(@PathVariable("potholeId") Long potholeId,
                                                                                  @Valid @RequestBody ReqPotChgPrgsStusPotMngrServDto changeProgressStatusRequestDto) {

        Pothole pothole = potholeManagerService.changePotholeProgressStatus(potholeId, changeProgressStatusRequestDto);

        return new BaseResponse<>(new RespPotSimInfoPotMngrCntrDto(pothole));
    }

    @DeleteMapping("/{potholeId}")
    public BaseResponse<Void> deletePothole(@PathVariable("potholeId") Long potholeId) {
        potholeManagerService.deletePothole(potholeId);

        return new BaseResponse<>();
    }

    @GetMapping("/potholes-filters")
    public BaseResponse<List<RespPotSimInfoPotMngrCntrDto>> getFilteredPotholes(@RequestParam(value = "minImportance", required = false) Integer minImportance,
                                                                                @RequestParam(value = "maxImportance", required = false) Integer maxImportance,
                                                                                @RequestParam(value = "potholeProgressStatus", required = false) Progress potholeProgressStatus,
                                                                                @RequestParam(value = "roadName", required = false) String roadName) {

        List<Pothole> filteredPotholes = potholeManagerService.getFilteredPotholes(new PotFltPotMngrServDto(minImportance, maxImportance, potholeProgressStatus, roadName));

        return new BaseResponse<>(filteredPotholes.stream()
                                                  .map(RespPotSimInfoPotMngrCntrDto::new)
                                                  .toList()
        );
    }
}
