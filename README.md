# kafka-sandbox


## Overview

This repository contains a sample project that demonstrates how to handle invalid timestamps pushed by a Kafka producer. The project includes custom timestamp interceptors for Kafka consumers and producers, helping to manage and validate timestamps effectively.

## Project Structure

Here's an overview of the files and directories present in this project:

- `.gitignore`: Specifies files and directories to be ignored by Git version control.
- `README.md`: This file, providing an introduction to the project and its structure.
- `build.sbt`: The build definition file for the project, written in Scala Build Tool (SBT) format.
- `project/build.properties`: Contains the version of SBT used by the project's build system.
- `project/plugins.sbt`: Lists the SBT plugins and their versions required for the project.
- `src/main/scala/CustomTimeStampInterceptors.scala`: Scala source code file containing the implementation of custom timestamp interceptors.
- `src/main/scala/kafkaConsumer.scala`: Scala source code file implementing the Kafka consumer with custom timestamp handling.
- `src/main/scala/kafkaProducer.scala`: Scala source code file implementing the Kafka producer with improved timestamp management.

## Usage

This project showcases how to handle invalid timestamps that might be pushed by a Kafka producer. The custom timestamp interceptors (`CustomTimeStampInterceptors.scala`) offer a solution for improving the handling of timestamps, ensuring accuracy and reliability within the Kafka messaging system.

To use this project:

1. Clone the repository to your local machine.
2. Ensure you have SBT (Scala Build Tool) installed.
3. Open a terminal and navigate to the project directory.
4. Review and modify the Kafka consumer and producer implementations according to your requirements.
5. Compile and run the project using SBT commands.

```bash
# Compile the project
sbt compile

# Run the Kafka consumer
sbt "runMain kafkaConsumer"

# Run the Kafka producer
sbt "runMain kafkaProducer"
