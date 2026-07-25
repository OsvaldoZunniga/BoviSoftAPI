package com.osvaldozunniga.apiSpringBoot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osvaldozunniga.apiSpringBoot.model.entity.Cattle;
import com.osvaldozunniga.apiSpringBoot.repository.CattleRepository;

@RestController
@RequestMapping("/cattle")
public class CattleController {
    
    @Autowired
    private CattleRepository cattleRepository;

    @GetMapping("/")
    public List<Cattle> getAllCattles(){
        return cattleRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Optional<Cattle> getCattleById(@PathVariable String id){
        return cattleRepository.findById(id);
    }
    @PostMapping("/")
    public Cattle createCattle(@RequestBody Cattle cattle){
        return cattleRepository.save(cattle);
    }
    @DeleteMapping("/{id}/destroy")
    public ResponseEntity<Cattle> destroyCattle(@PathVariable String id){
        return cattleRepository.findById(id)
        .map( cattle ->{
            cattle.setStatusId(0);
            return ResponseEntity.ok(cattleRepository.save(cattle));
        })
        .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Cattle> updateCattle(@RequestBody Cattle cattleDetails, @PathVariable String id){
        return cattleRepository.findById(id)
        .map( existingCattle ->{
            existingCattle.setName(cattleDetails.getName());
            existingCattle.setBirthday(cattleDetails.getBirthday());
            existingCattle.setBreed(cattleDetails.getBreed());
            existingCattle.setEarTag(cattleDetails.getEarTag());
            existingCattle.setWeight(cattleDetails.getWeight());
            existingCattle.setImagePath(cattleDetails.getImagePath());
            Cattle updatedCattle = cattleRepository.save(existingCattle);

            return ResponseEntity.ok(updatedCattle);
        })
        .orElse(ResponseEntity.notFound().build());
    }
}
