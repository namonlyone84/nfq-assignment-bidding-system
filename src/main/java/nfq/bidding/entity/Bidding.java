package nfq.bidding.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "bidding")
public class Bidding {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "bidPrice")
    @NotNull
    private BigDecimal bidPrice;

    @ManyToOne
    @JoinColumn(name = "jobId")
    @NotNull
    @JsonIgnore
    private Job biddingJob;

    @ManyToOne
    @JoinColumn(name = "userId")
    @NotNull
    @JsonIgnore
    private User user;
}