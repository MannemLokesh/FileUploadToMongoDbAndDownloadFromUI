package com.hrportal.exafluence;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hrportal.exafluence.document.FileModel;

public interface FileRepository extends MongoRepository<FileModel, String>
{

}
