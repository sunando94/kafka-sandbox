name := "Kafka-Sandbox"

version := "0.1"

scalaVersion := "2.12.14"

idePackagePrefix := Some("com.databricks")

// spark
lazy val sparkVersion = "3.2.1"
lazy val sparkSQLProvided = "org.apache.spark" %% "spark-sql" % sparkVersion % Provided


lazy val databricksCommonDeps = Seq(sparkSQLProvided)

libraryDependencies ++= databricksCommonDeps
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "3.5.1"

assemblyMergeStrategy in assembly := {
  case x if x.contains("META-INF") => MergeStrategy.discard
  case x if (x.contains("codegen-resources/service-2.json")
    | x.contains("module-info.class")
    | x.contains("codegen-resources/customization.config")
    | x.contains("codegen-resources/paginators-1.json")
    | x.contains("codegen-resources/waiters-2.json")) => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}