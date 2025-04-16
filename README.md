# KafkaJ

KafkaJ is an IntelliJ IDEA plugin that replaces the standard progress bar with a Kafka-themed progress bar. It customizes the loading indicators throughout the IDE with a purple Kafka-themed design.

<!-- Consider adding a screenshot of the plugin in action here -->
<!-- Example: ![Kafka Progress Bar](screenshots/preview.png) -->

## Features

- Replaces the standard IntelliJ IDEA progress bar with a Kafka-themed progress bar
- Customizes loading indicators with a purple Kafka-themed design
- Includes an animated Kafka logo that moves along the progress bar
- Applies Kafka purple color theme to various UI elements throughout the IDE

## Installation

### From JetBrains Marketplace

1. Open IntelliJ IDEA
2. Go to Settings/Preferences > Plugins
3. Click on "Marketplace" tab
4. Search for "KafkaJ"
5. Click "Install"
6. Restart IntelliJ IDEA when prompted

### Manual Installation

1. Download the latest release from the [GitHub Releases](https://github.com/edivad1999/kafkaJ/releases) page
2. Open IntelliJ IDEA
3. Go to Settings/Preferences > Plugins
4. Click on the gear icon and select "Install Plugin from Disk..."
5. Navigate to the downloaded .zip file and select it
6. Restart IntelliJ IDEA when prompted

## Requirements

- IntelliJ IDEA 2023.1 or newer (build 231.* or newer)
- Java 17 or newer

## Usage

Once installed, the plugin automatically replaces the standard progress bar with the Kafka-themed progress bar. No additional configuration is required.

## Building from Source

To build the plugin from source:

1. Clone the repository
   ```
   git clone https://github.com/edivad1999/kafkaJ.git
   ```
2. Navigate to the project directory
   ```
   cd kafkaJ
   ```
3. Build using Gradle
   ```
   ./gradlew buildPlugin
   ```
4. The built plugin will be available in `build/distributions/`


## License

No explicit license has been specified for this project. The Gradle wrapper files are under the Apache License 2.0, but this is standard for Gradle wrappers and may not reflect the license of the project itself.

Please contact the author for licensing information.

## Author

- [Davide Magli](https://github.com/edivad1999)

## Acknowledgements

- Apache Kafka for the inspiration
- JetBrains for the IntelliJ Platform SDK
