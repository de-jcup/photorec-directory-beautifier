package de.jcup.pdb;

import java.io.File;

import com.beust.jcommander.IStringConverter;

public class FileConverter implements IStringConverter<File> {
  @Override
  public File convert(String value) {
    return new File(value);
  }
}