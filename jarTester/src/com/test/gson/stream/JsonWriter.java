/*     */ package com.test.gson.stream;
/*     */ 
/*     */ import com.test.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.test.gson.FormattingStyle;
/*     */ import com.test.gson.Strictness;
/*     */ import java.io.Closeable;
/*     */ import java.io.Flushable;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonWriter
/*     */   implements Closeable, Flushable
/*     */ {
/* 167 */   private static final Pattern VALID_JSON_NUMBER_PATTERN = Pattern.compile("-?(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?(?:[eE][-+]?[0-9]+)?");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   private static final String[] REPLACEMENT_CHARS = new String[128]; static {
/* 184 */     for (int i = 0; i <= 31; i++) {
/* 185 */       REPLACEMENT_CHARS[i] = String.format("\\u%04x", new Object[] { Integer.valueOf(i) });
/*     */     } 
/* 187 */     REPLACEMENT_CHARS[34] = "\\\"";
/* 188 */     REPLACEMENT_CHARS[92] = "\\\\";
/* 189 */     REPLACEMENT_CHARS[9] = "\\t";
/* 190 */     REPLACEMENT_CHARS[8] = "\\b";
/* 191 */     REPLACEMENT_CHARS[10] = "\\n";
/* 192 */     REPLACEMENT_CHARS[13] = "\\r";
/* 193 */     REPLACEMENT_CHARS[12] = "\\f";
/* 194 */   } private static final String[] HTML_SAFE_REPLACEMENT_CHARS = (String[])REPLACEMENT_CHARS.clone(); static {
/* 195 */     HTML_SAFE_REPLACEMENT_CHARS[60] = "\\u003c";
/* 196 */     HTML_SAFE_REPLACEMENT_CHARS[62] = "\\u003e";
/* 197 */     HTML_SAFE_REPLACEMENT_CHARS[38] = "\\u0026";
/* 198 */     HTML_SAFE_REPLACEMENT_CHARS[61] = "\\u003d";
/* 199 */     HTML_SAFE_REPLACEMENT_CHARS[39] = "\\u0027";
/*     */   }
/*     */ 
/*     */   
/*     */   private final Writer out;
/*     */   
/* 205 */   private int[] stack = new int[32];
/* 206 */   private int stackSize = 0; private FormattingStyle formattingStyle; private String formattedColon;
/*     */   
/*     */   public JsonWriter(Writer out) {
/* 209 */     push(6);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     this.strictness = Strictness.LEGACY_STRICT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     this.serializeNulls = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     this.out = Objects.<Writer>requireNonNull(out, "out == null");
/* 234 */     setFormattingStyle(FormattingStyle.COMPACT);
/*     */   }
/*     */ 
/*     */   
/*     */   private String formattedComma;
/*     */   
/*     */   private boolean usesEmptyNewlineAndIndent;
/*     */   
/*     */   private Strictness strictness;
/*     */   
/*     */   private boolean htmlSafe;
/*     */   
/*     */   private String deferredName;
/*     */   private boolean serializeNulls;
/*     */   
/*     */   public final void setIndent(String indent) {
/* 250 */     if (indent.isEmpty()) {
/* 251 */       setFormattingStyle(FormattingStyle.COMPACT);
/*     */     } else {
/* 253 */       setFormattingStyle(FormattingStyle.PRETTY.withIndent(indent));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setFormattingStyle(FormattingStyle formattingStyle) {
/* 267 */     this.formattingStyle = Objects.<FormattingStyle>requireNonNull(formattingStyle);
/*     */     
/* 269 */     this.formattedComma = ",";
/* 270 */     if (this.formattingStyle.usesSpaceAfterSeparators()) {
/* 271 */       this.formattedColon = ": ";
/*     */ 
/*     */       
/* 274 */       if (this.formattingStyle.getNewline().isEmpty()) {
/* 275 */         this.formattedComma = ", ";
/*     */       }
/*     */     } else {
/* 278 */       this.formattedColon = ":";
/*     */     } 
/*     */     
/* 281 */     this
/* 282 */       .usesEmptyNewlineAndIndent = (this.formattingStyle.getNewline().isEmpty() && this.formattingStyle.getIndent().isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FormattingStyle getFormattingStyle() {
/* 292 */     return this.formattingStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void setLenient(boolean lenient) {
/* 313 */     setStrictness(lenient ? Strictness.LENIENT : Strictness.LEGACY_STRICT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLenient() {
/* 322 */     return (this.strictness == Strictness.LENIENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setStrictness(Strictness strictness) {
/* 345 */     this.strictness = Objects.<Strictness>requireNonNull(strictness);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Strictness getStrictness() {
/* 355 */     return this.strictness;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setHtmlSafe(boolean htmlSafe) {
/* 365 */     this.htmlSafe = htmlSafe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isHtmlSafe() {
/* 372 */     return this.htmlSafe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSerializeNulls(boolean serializeNulls) {
/* 380 */     this.serializeNulls = serializeNulls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean getSerializeNulls() {
/* 388 */     return this.serializeNulls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter beginArray() throws IOException {
/* 399 */     writeDeferredName();
/* 400 */     return openScope(1, '[');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter endArray() throws IOException {
/* 410 */     return closeScope(1, 2, ']');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter beginObject() throws IOException {
/* 421 */     writeDeferredName();
/* 422 */     return openScope(3, '{');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter endObject() throws IOException {
/* 432 */     return closeScope(3, 5, '}');
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private JsonWriter openScope(int empty, char openBracket) throws IOException {
/* 438 */     beforeValue();
/* 439 */     push(empty);
/* 440 */     this.out.write(openBracket);
/* 441 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private JsonWriter closeScope(int empty, int nonempty, char closeBracket) throws IOException {
/* 447 */     int context = peek();
/* 448 */     if (context != nonempty && context != empty) {
/* 449 */       throw new IllegalStateException("Nesting problem.");
/*     */     }
/* 451 */     if (this.deferredName != null) {
/* 452 */       throw new IllegalStateException("Dangling name: " + this.deferredName);
/*     */     }
/*     */     
/* 455 */     this.stackSize--;
/* 456 */     if (context == nonempty) {
/* 457 */       newline();
/*     */     }
/* 459 */     this.out.write(closeBracket);
/* 460 */     return this;
/*     */   }
/*     */   
/*     */   private void push(int newTop) {
/* 464 */     if (this.stackSize == this.stack.length) {
/* 465 */       this.stack = Arrays.copyOf(this.stack, this.stackSize * 2);
/*     */     }
/* 467 */     this.stack[this.stackSize++] = newTop;
/*     */   }
/*     */ 
/*     */   
/*     */   private int peek() {
/* 472 */     if (this.stackSize == 0) {
/* 473 */       throw new IllegalStateException("JsonWriter is closed.");
/*     */     }
/* 475 */     return this.stack[this.stackSize - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   private void replaceTop(int topOfStack) {
/* 480 */     this.stack[this.stackSize - 1] = topOfStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter name(String name) throws IOException {
/* 491 */     Objects.requireNonNull(name, "name == null");
/* 492 */     if (this.deferredName != null) {
/* 493 */       throw new IllegalStateException("Already wrote a name, expecting a value.");
/*     */     }
/* 495 */     int context = peek();
/* 496 */     if (context != 3 && context != 5) {
/* 497 */       throw new IllegalStateException("Please begin an object before writing a name.");
/*     */     }
/* 499 */     this.deferredName = name;
/* 500 */     return this;
/*     */   }
/*     */   
/*     */   private void writeDeferredName() throws IOException {
/* 504 */     if (this.deferredName != null) {
/* 505 */       beforeName();
/* 506 */       string(this.deferredName);
/* 507 */       this.deferredName = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter value(String value) throws IOException {
/* 519 */     if (value == null) {
/* 520 */       return nullValue();
/*     */     }
/* 522 */     writeDeferredName();
/* 523 */     beforeValue();
/* 524 */     string(value);
/* 525 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter value(boolean value) throws IOException {
/* 535 */     writeDeferredName();
/* 536 */     beforeValue();
/* 537 */     this.out.write(value ? "true" : "false");
/* 538 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter value(Boolean value) throws IOException {
/* 549 */     if (value == null) {
/* 550 */       return nullValue();
/*     */     }
/* 552 */     writeDeferredName();
/* 553 */     beforeValue();
/* 554 */     this.out.write(value.booleanValue() ? "true" : "false");
/* 555 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter value(float value) throws IOException {
/* 570 */     writeDeferredName();
/* 571 */     if (this.strictness != Strictness.LENIENT && (Float.isNaN(value) || Float.isInfinite(value))) {
/* 572 */       throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
/*     */     }
/* 574 */     beforeValue();
/* 575 */     this.out.append(Float.toString(value));
/* 576 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter value(double value) throws IOException {
/* 590 */     writeDeferredName();
/* 591 */     if (this.strictness != Strictness.LENIENT && (Double.isNaN(value) || Double.isInfinite(value))) {
/* 592 */       throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
/*     */     }
/* 594 */     beforeValue();
/* 595 */     this.out.append(Double.toString(value));
/* 596 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter value(long value) throws IOException {
/* 606 */     writeDeferredName();
/* 607 */     beforeValue();
/* 608 */     this.out.write(Long.toString(value));
/* 609 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter value(Number value) throws IOException {
/* 625 */     if (value == null) {
/* 626 */       return nullValue();
/*     */     }
/*     */     
/* 629 */     writeDeferredName();
/* 630 */     String string = value.toString();
/* 631 */     if (string.equals("-Infinity") || string.equals("Infinity") || string.equals("NaN")) {
/* 632 */       if (this.strictness != Strictness.LENIENT) {
/* 633 */         throw new IllegalArgumentException("Numeric values must be finite, but was " + string);
/*     */       }
/*     */     } else {
/* 636 */       Class<? extends Number> numberClass = (Class)value.getClass();
/*     */       
/* 638 */       if (!isTrustedNumberType(numberClass) && 
/* 639 */         !VALID_JSON_NUMBER_PATTERN.matcher(string).matches()) {
/* 640 */         throw new IllegalArgumentException("String created by " + numberClass + " is not a valid JSON number: " + string);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 645 */     beforeValue();
/* 646 */     this.out.append(string);
/* 647 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter nullValue() throws IOException {
/* 657 */     if (this.deferredName != null) {
/* 658 */       if (this.serializeNulls) {
/* 659 */         writeDeferredName();
/*     */       } else {
/* 661 */         this.deferredName = null;
/* 662 */         return this;
/*     */       } 
/*     */     }
/* 665 */     beforeValue();
/* 666 */     this.out.write("null");
/* 667 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public JsonWriter jsonValue(String value) throws IOException {
/* 682 */     if (value == null) {
/* 683 */       return nullValue();
/*     */     }
/* 685 */     writeDeferredName();
/* 686 */     beforeValue();
/* 687 */     this.out.append(value);
/* 688 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 696 */     if (this.stackSize == 0) {
/* 697 */       throw new IllegalStateException("JsonWriter is closed.");
/*     */     }
/* 699 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 709 */     this.out.close();
/*     */     
/* 711 */     int size = this.stackSize;
/* 712 */     if (size > 1 || (size == 1 && this.stack[size - 1] != 7)) {
/* 713 */       throw new IOException("Incomplete document");
/*     */     }
/* 715 */     this.stackSize = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isTrustedNumberType(Class<? extends Number> c) {
/* 725 */     return (c == Integer.class || c == Long.class || c == Double.class || c == Float.class || c == Byte.class || c == Short.class || c == BigDecimal.class || c == BigInteger.class || c == AtomicInteger.class || c == AtomicLong.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void string(String value) throws IOException {
/* 738 */     String[] replacements = this.htmlSafe ? HTML_SAFE_REPLACEMENT_CHARS : REPLACEMENT_CHARS;
/* 739 */     this.out.write(34);
/* 740 */     int last = 0;
/* 741 */     int length = value.length();
/* 742 */     for (int i = 0; i < length; i++) {
/* 743 */       String replacement; char c = value.charAt(i);
/*     */       
/* 745 */       if (c < '') {
/* 746 */         replacement = replacements[c];
/* 747 */         if (replacement == null) {
/*     */           continue;
/*     */         }
/* 750 */       } else if (c == ' ') {
/* 751 */         replacement = "\\u2028";
/* 752 */       } else if (c == ' ') {
/* 753 */         replacement = "\\u2029";
/*     */       } else {
/*     */         continue;
/*     */       } 
/* 757 */       if (last < i) {
/* 758 */         this.out.write(value, last, i - last);
/*     */       }
/* 760 */       this.out.write(replacement);
/* 761 */       last = i + 1; continue;
/*     */     } 
/* 763 */     if (last < length) {
/* 764 */       this.out.write(value, last, length - last);
/*     */     }
/* 766 */     this.out.write(34);
/*     */   }
/*     */   
/*     */   private void newline() throws IOException {
/* 770 */     if (this.usesEmptyNewlineAndIndent) {
/*     */       return;
/*     */     }
/*     */     
/* 774 */     this.out.write(this.formattingStyle.getNewline());
/* 775 */     for (int i = 1, size = this.stackSize; i < size; i++) {
/* 776 */       this.out.write(this.formattingStyle.getIndent());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void beforeName() throws IOException {
/* 785 */     int context = peek();
/* 786 */     if (context == 5) {
/* 787 */       this.out.write(this.formattedComma);
/* 788 */     } else if (context != 3) {
/* 789 */       throw new IllegalStateException("Nesting problem.");
/*     */     } 
/* 791 */     newline();
/* 792 */     replaceTop(4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void beforeValue() throws IOException {
/* 801 */     switch (peek()) {
/*     */       case 7:
/* 803 */         if (this.strictness != Strictness.LENIENT) {
/* 804 */           throw new IllegalStateException("JSON must have only one top-level value.");
/*     */         }
/*     */       
/*     */       case 6:
/* 808 */         replaceTop(7);
/*     */         return;
/*     */       
/*     */       case 1:
/* 812 */         replaceTop(2);
/* 813 */         newline();
/*     */         return;
/*     */       
/*     */       case 2:
/* 817 */         this.out.append(this.formattedComma);
/* 818 */         newline();
/*     */         return;
/*     */       
/*     */       case 4:
/* 822 */         this.out.append(this.formattedColon);
/* 823 */         replaceTop(5);
/*     */         return;
/*     */     } 
/*     */     
/* 827 */     throw new IllegalStateException("Nesting problem.");
/*     */   }
/*     */ }


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/stream/JsonWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */