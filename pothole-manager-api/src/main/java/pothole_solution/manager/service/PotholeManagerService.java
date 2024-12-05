package pothole_solution.manager.service;

import org.springframework.web.multipart.MultipartFile;
import pothole_solution.core.domain.pothole.dto.PotFltPotMngrServDto;
import pothole_solution.core.domain.pothole.dto.request.ReqPotChgPrgsStusPotMngrServDto;
import pothole_solution.core.domain.pothole.dto.request.ReqPotRegPotMngrServDto;
import pothole_solution.core.domain.pothole.entity.Pothole;

import java.util.List;

public interface PotholeManagerService {
    Pothole registerPothole(ReqPotRegPotMngrServDto reqPotRegPotMngrServDto, List<MultipartFile> potholeImages);

    Pothole getPotholeWithPotholeHistoryByPotholeId(Long potholeId);

    List<Pothole> getAllPotholes();

    Pothole changePotholeProgressStatus(Long potholeId, ReqPotChgPrgsStusPotMngrServDto changePotholeProcessStatusRequestDto);

    void deletePothole(Long potholeId);

    List<Pothole> getFilteredPotholes(PotFltPotMngrServDto potFltPotMngrServDto);
}
