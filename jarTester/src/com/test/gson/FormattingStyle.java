/*     */ package com.test.gson;
/*     */ 
/*     */ import java.util.Objects;
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
/*     */ public class FormattingStyle
/*     */ {
/*     */   private final String newline;
/*     */   private final String indent;
/*     */   private final boolean spaceAfterSeparators;
/*  53 */   public static final FormattingStyle COMPACT = new FormattingStyle("", "", false);
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
/*  64 */   public static final FormattingStyle PRETTY = new FormattingStyle("\n", "  ", true);
/*     */   
/*     */   private FormattingStyle(String newline, String indent, boolean spaceAfterSeparators) {
/*  67 */     Objects.requireNonNull(newline, "newline == null");
/*  68 */     Objects.requireNonNull(indent, "indent == null");
/*  69 */     if (!newline.matches("[\r\n]*")) {
/*  70 */       throw new IllegalArgumentException("Only combinations of \\n and \\r are allowed in newline.");
/*     */     }
/*     */     
/*  73 */     if (!indent.matches("[ \t]*")) {
/*  74 */       throw new IllegalArgumentException("Only combinations of spaces and tabs are allowed in indent.");
/*     */     }
/*     */     
/*  77 */     this.newline = newline;
/*  78 */     this.indent = indent;
/*  79 */     this.spaceAfterSeparators = spaceAfterSeparators;
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
/*     */   public FormattingStyle withNewline(String newline) {
/*  95 */     return new FormattingStyle(newline, this.indent, this.spaceAfterSeparators);
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
/*     */   public FormattingStyle withIndent(String indent) {
/* 107 */     return new FormattingStyle(this.newline, indent, this.spaceAfterSeparators);
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
/*     */   public FormattingStyle withSpaceAfterSeparators(boolean spaceAfterSeparators) {
/* 122 */     return new FormattingStyle(this.newline, this.indent, spaceAfterSeparators);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNewline() {
/* 131 */     return this.newline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIndent() {
/* 140 */     return this.indent;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean usesSpaceAfterSeparators() {
/* 145 */     return this.spaceAfterSeparators;
/*     */   }
/*     */ }


/* Location:              /Users/nhn/test/gson-2.11.0.jar!/com/google/gson/FormattingStyle.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */