JAVA_HOME ?= /usr/lib/jvm/java-17-openjdk-amd64
GRADLE=./gradlew
BUILD_TASK=shadowJar
CLEAN_TASK=clean
BUILD_DIR=build/libs/
ARTIFACT_NAME=Rogue-1.0.jar

SD_PATH=temp/jcurses/lib
SD_NAME=libjcurses.so

NCURSES_NAME=libncurses.so.6.3
NCURSES_PATH=/usr/lib/x86_64-linux-gnu/

# Получаем WSL-путь к проекту автоматически
PROJECT_DIR_WSL ?= $(shell wslpath -a '$(CURDIR)' | tr -d '\r')

default: run
all: build run

clean:
	$(GRADLE) $(CLEAN_TASK)
	rm -rf temp

build: clean
	$(GRADLE) $(BUILD_TASK)

# Обычный запуск (в терминале)
run: build
	java -Djava.library.path=/usr/java/packages/lib:/usr/lib/x86_64-linux-gnu -jar $(BUILD_DIR)$(ARTIFACT_NAME)

run-wsl:
	@echo "Открытие нового окна WSL для сборки и запуска..."
	powershell -Command "Start-Process wt -ArgumentList '-w 0 new-tab --profile \"Ubuntu\" --title \"JCurses Build & Run\" wsl bash -c \"cd '$(PROJECT_DIR_WSL)' && $(GRADLE) $(CLEAN_TASK) $(BUILD_TASK) && java -Djava.library.path=/usr/java/packages/lib:/usr/lib/x86_64-linux-gnu -jar $(BUILD_DIR)$(ARTIFACT_NAME); read -p \\\"Нажмите Enter для выхода...\\\"\"'"
	
lib_install:
	rm -rf temp
	mkdir -p temp
	cd ./temp && wget -O jcurses-linux-0.9.5b.tar.gz https://sourceforge.net/projects/javacurses/files/javacurses/0.9.5b/jcurses-linux-0.9.5b.tar.gz/download && tar -xf jcurses-linux-0.9.5b.tar.gz
	mkdir -p $(SD_PATH)
	mv ./temp/jcurses/lib/libjcurses64.so $(SD_PATH)/$(SD_NAME)
	sudo mkdir -p /usr/java/packages/lib
	sudo cp $(SD_PATH)/$(SD_NAME) /usr/java/packages/lib/
	sudo ln -sf $(NCURSES_PATH)$(NCURSES_NAME) $(NCURSES_PATH)libncurses.so.5
	rm -rf temp

.PHONY: clean build run run-idea lib_install default all
