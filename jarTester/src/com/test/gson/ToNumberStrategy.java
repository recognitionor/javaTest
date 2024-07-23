package com.test.gson;

import com.test.gson.stream.JsonReader;
import java.io.IOException;

public interface ToNumberStrategy {
  Number readNumber(JsonReader paramJsonReader) throws IOException;
}


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/ToNumberStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */