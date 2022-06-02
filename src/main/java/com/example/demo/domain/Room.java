package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Transactional
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String roomName;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Locker> lockers = new ArrayList<Locker>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
