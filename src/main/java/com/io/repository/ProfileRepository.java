package com.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.io.domain.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

	Profile findById(Long id);

}