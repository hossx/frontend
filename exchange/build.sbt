name := """coinport-frontend"""

version := "1.0.1-SNAPSHOT"

resolvers ++= Seq(
  "Nexus Snapshots" at "http://192.168.0.105:8081/nexus/content/groups/public/",
  "jahia org repository" at "http://maven.jahia.org/maven2/",
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= {
  val akkaVersion = "2.3.0"
  Seq(
    "com.typesafe.akka"           %% "akka-remote"                      % akkaVersion,
    "com.typesafe.akka"           %% "akka-cluster"                     % akkaVersion,
    "com.typesafe.akka"           %% "akka-slf4j"                       % akkaVersion,
    "com.typesafe.akka"           %% "akka-contrib"                     % akkaVersion,
    "com.typesafe.akka"           %% "akka-testkit"                     % akkaVersion,
    "org.json4s"                  %% "json4s-native"                    % "3.2.8",
    "org.json4s"                  %% "json4s-ext"                       % "3.2.8",
    "com.github.tototoshi"        %% "play-json4s-native"               % "0.2.0",
    "com.github.tototoshi"        %% "play-json4s-test-native"          % "0.2.0" % "test",
    "com.coinport"                %% "coinex-client"                    % "1.1.10-SNAPSHOT",
    "com.octo.captcha"            %  "jcaptcha"                         % "1.0",
    "org.apache.hadoop"           %  "hadoop-core"                      % "1.1.2",
    "org.apache.hadoop"           %  "hadoop-client"                    % "1.1.2",
    "org.webjars"                 %  "bootstrap"                        % "3.1.1",
    "com.twilio.sdk"              %  "twilio-java-sdk"                  % "3.4.1"
  )
}

play.Project.playScalaSettings
