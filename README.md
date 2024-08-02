# BLE (Bluetooth Low Energy) Beacon Scanner

This repository contains an Android application that scans Bluetooth Low Energy (BLE) beacons such as iBeacon and Eddystone, parses their data, and displays the results in a RecyclerView.

## Features

- **BLE Scanning**: Identifies and scans nearby BLE beacons.
- **Multiple Beacon Types**: Supports iBeacon, Eddystone UID, Eddystone URL, Eddystone TLM, and Eddystone EID.
- **Dynamic UI**: Displays scanned data in a RecyclerView with dynamic layouts based on the beacon type.
- **Real-time Data**: Continuously updates the list of scanned devices as new beacons are detected.

## Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/MajorAbdullah/BLE.git
    ```
2. **Open in Android Studio**: Import the project and sync the Gradle files.
3. **Run on an Android device**: Ensure that Bluetooth and location services are enabled on the device.

## Usage

1. **Enable Bluetooth**: Ensure Bluetooth is turned on.
2. **Start Scanning**: Press the scan button to discover nearby BLE beacons.
3. **View Beacon Data**: The app will display detected beacons and their information in a list.

## Code Structure

- **MainActivity.java**: Initializes the Bluetooth adapter, handles permissions, and manages the scanning process.
- **ExampleAdapter.java**: Binds beacon data to the RecyclerView.
- **ExampleItem.java**: Represents the structure of the beacon data.

## Screenshots

![Scanning Screen](https://yourlink.com/screenshot1.png)
![Beacon Details](https://yourlink.com/screenshot2.png)

## Dependencies

- **neovisionaries Bluetooth Library**: For parsing BLE advertisement data.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

- **GitHub**: [MajorAbdullah](https://github.com/MajorAbdullah)
- **Email**: sa.abdullahshah.2001@gmail.com
