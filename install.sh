#!/bin/bash

trap "exit" INT

DIR="spigotInstall"
if [ -d "$DIR" ]; then
   echo "$DIR already created" > /dev/null
else
   mkdir -p $DIR
fi
cd "$DIR" || exit


case "$OSTYPE" in
  solaris*) os=solaris ;;
  darwin*)  os=macos ;;
  linux*)   os=linux ;;
  bsd*)     os=bsd ;;
  msys*)    os=windows ;;
  cygwin*)  os=windows ;;
  *)        os=unknown:$OSTYPE ;;
esac

extension=.tar.gz
executable=

if [[ $os == "windows" ]]; then
   extension=.zip
   executable=.exe
   export IFS=";"
fi
echo "OS used: $os with $extension"
echo "Check in $JAVA_HOME for java versions..."

# Versions we care about
java21=unknown
# Future versions can be added, e.g.: java23=unknown

function installJDK() {
   echo "Failed to find Java $1. Downloading it..."
   curl -L -o "Java$1$extension" "$2"
   mkdir "jdk-$1"
   if [[ $extension == ".zip" ]]; then
      unzip "Java$1$extension" -d "jdk-$1"
   else
      tar -xvf "Java$1$extension" -C "jdk-$1" --strip-components 1
   fi
   rm "Java$1$extension"
}

# Detect installed Java versions
for javaVersionPath in $JAVA_HOME; do
   if [[ $javaVersionPath == *"21."* ]]; then
      java21="$javaVersionPath/java$executable"
      eval "\"$java21\" -version" || java21=unknown
   fi
   # Future check:
   # elif [[ $javaVersionPath == *"23."* ]]; then
   #    java23="$javaVersionPath/java$executable"
   #    eval "\"$java23\" -version" || java23=unknown
   # fi
done

# Install Java 21 if missing
if [[ $java21 == *"unknown"* ]]; then
   if [ -d "./jdk-21" ]; then
      echo "Java 21 already downloaded, using this version..."
   else
      installJDK 21 "https://download.oracle.com/java/21/latest/jdk-21_$os-x64_bin$extension"
   fi
   java21="$PWD/jdk-21/bin/java$executable"
fi

echo "Using java21: $java21"

echo "-------- WARN --------"
echo ""
echo "This will take some time to complete. Please wait until it's finished."
echo ""
echo "-------- WARN --------"

sleep 3s

curl -o BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar

# List of supported Minecraft versions
for mcVersion in "1.20.6" "1.21.10"; do
   snap="$mcVersion-R0.1-SNAPSHOT"
   spigotRepository="$HOME/.m2/repository/org/spigotmc/spigot/$snap/spigot-$snap"
   mcSrvRepository="$HOME/.m2/repository/org/spigotmc/minecraft-server/$snap/minecraft-server-$snap"

   shouldRun=false
   for repoFile in "$spigotRepository.jar" "$mcSrvRepository.jar"; do
      if [ ! -f "$repoFile" ]; then
         shouldRun=true
         echo "Failed to find file $repoFile."
      fi
   done

   if [ $shouldRun == true ]; then
      if [ -d "$HOME/.m2/repository/org/spigotmc/spigot/$snap" ]; then
         echo "Found incomplete repository for $mcVersion. Running remapping BuildTools ..."
      fi
      mcCmd="\"$java21\" -jar BuildTools.jar --rev $mcVersion --remapped"
      echo "Running $mcCmd ..."
      eval $mcCmd
      if [ ! -f "spigot-$mcVersion.jar" ]; then
         echo "Failed to build version $mcVersion."
         exit 1
      fi
   else
      echo "Repository for $mcVersion already exists. Skipping..."
   fi
done
echo "Installation complete!"
