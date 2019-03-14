package nfq.bidding.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import nfq.bidding.constant.JobStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "job")
public class Job {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "caption")
    private String caption;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "currencyCode")
    private String currencyCode;

    @ManyToOne
    @JoinColumn(name = "userId")
    @NotNull
    @JsonIgnore
    private User user;
}
