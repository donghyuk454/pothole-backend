package pothole_solution.core.domain.pothole.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import pothole_solution.core.domain.BaseTimeEntity;
import pothole_solution.core.global.util.converter.ProgressEnumConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Pothole extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long potholeId;

    @Column(length = 50)
    private String roadAddress;         // 전체 도로명 주소
    private String roadName;            // 도로명
    private String roadNumber;          // 도로 번호
    private String zipCode;             // 우편 번호

    @Column(nullable = false, columnDefinition = "geography(Point, 4326)")
    private Point point;

    @Column(columnDefinition = "TEXT")
    private String thumbnail;

    private Integer importance;
    private Integer dangerous;

    @Column(nullable = false, length = 5)
    @Convert(converter = ProgressEnumConverter.class)
    private Progress processStatus;

    @OneToMany(mappedBy = "pothole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PotholeHistory> potholeHistories = new ArrayList<>();

    @Builder
    public Pothole(Point point, String thumbnail, Integer importance, Progress processStatus, Integer dangerous) {
        this.point = point;
        this.thumbnail = thumbnail;
        this.importance = importance;
        this.dangerous = dangerous;
        this.processStatus = processStatus;
    }

    public void changeProgress(Progress processStatus) {
        this.processStatus = processStatus;
    }

    public void createThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void changeThumbnail(String newThumbnail) {
        this.thumbnail = newThumbnail;
    }

    public void initAddress(String roadAddress, String roadName, String zipCode, String roadNumber) {
        this.roadAddress = roadAddress;
        this.roadName = roadName;
        this.zipCode = zipCode;
        this.roadNumber = roadNumber;
    }
}
