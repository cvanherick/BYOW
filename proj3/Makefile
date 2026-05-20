LIB_DIR := ../../library-fa24
OUT_DIR := out/classes
TEST_OUT_DIR := out/test-classes
DIST_DIR := dist
CP := "$(LIB_DIR)/*:$(OUT_DIR)"

SOURCES := $(shell find src -name '*.java')
TEST_SOURCES := $(shell find tests -name '*.java')

.PHONY: compile compile-tests jar run clean

compile:
	mkdir -p $(OUT_DIR)
	javac -cp "$(LIB_DIR)/*" -d $(OUT_DIR) $(SOURCES)

compile-tests: compile
	mkdir -p $(TEST_OUT_DIR)
	javac -cp "$(LIB_DIR)/*:$(OUT_DIR)" -d $(TEST_OUT_DIR) $(TEST_SOURCES)

jar: compile
	mkdir -p $(DIST_DIR)
	cp "$(LIB_DIR)/algs4.jar" "$(DIST_DIR)/byow-demo.jar"
	jar uf "$(DIST_DIR)/byow-demo.jar" -C $(OUT_DIR) .
	printf "Main-Class: core.Main\n" > "$(DIST_DIR)/manifest.txt"
	jar ufm "$(DIST_DIR)/byow-demo.jar" "$(DIST_DIR)/manifest.txt"

run: compile
	java -cp $(CP) core.Main

clean:
	rm -rf out dist
