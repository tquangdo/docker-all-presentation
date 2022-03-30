package com.example.SpringBootDocker.repositories;

import com.example.SpringBootDocker.models.Student;
import org.springframework.data.repository.CrudRepository;

//Inject this repository to controller
public interface StudentRepository
        extends CrudRepository<Student, Integer> {

}
