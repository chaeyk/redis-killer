package net.chaeyk.rediskiller;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@Table(name = "member")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "default")
public class MemberEntity {
	@Id
	private long memberId;

	@Column
	private String nickname;

	@Column(updatable = false)
	@CreationTimestamp
	private Instant crtDt;
	@Column
	@UpdateTimestamp
	private Instant updDt;
}

