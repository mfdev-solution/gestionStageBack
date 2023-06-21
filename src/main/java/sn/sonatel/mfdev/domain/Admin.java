package sn.sonatel.mfdev.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
//@NoArgsConstructor
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {}
