/*
 * Copyright 2021 The Error Prone Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.errorprone.annotations;

/**
 * Modifiers in the Java language, as specified in:
 *
 * <ul>
 *   <li>https://docs.oracle.com/javase/specs/jls/se11/html/jls-8.html#jls-8.1.1
 *   <li>https://docs.oracle.com/javase/specs/jls/se11/html/jls-8.html#jls-8.3.1
 *   <li>https://docs.oracle.com/javase/specs/jls/se11/html/jls-8.html#jls-8.4.3
 *   <li>https://docs.oracle.com/javase/specs/jls/se11/html/jls-9.html#jls-9.4
 * </ul>
 */
public enum Modifier {
    PUBLIC,
    PROTECTED,
    PRIVATE,
    ABSTRACT,
    DEFAULT,
    STATIC,
    FINAL,
    TRANSIENT,
    VOLATILE,
    SYNCHRONIZED,
    NATIVE,
    STRICTFP
}
