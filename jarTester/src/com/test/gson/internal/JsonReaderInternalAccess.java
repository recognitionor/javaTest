package com.test.gson.internal;

import com.test.gson.stream.JsonReader;
import java.io.IOException;

public abstract class JsonReaderInternalAccess {
  public static volatile JsonReaderInternalAccess INSTANCE;
  
  public abstract void promoteNameToValue(JsonReader paramJsonReader) throws IOException;
}


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/internal/JsonReaderInternalAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */