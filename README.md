# SmitchTask

# Android mDNS Service Scanner

This Android app allows you to publish mDNS services of type `_http._tcp` and scan for available services on the network. It also provides the functionality to scan for nearby BLE devices and initiate pairing.

## Features

- Publish mDNS services of type `_http._tcp` through the app.
- Scan the network for available mDNS services of type `_http._tcp` and display the details.
- Scan for nearby BLE devices and initiate pairing.

## Prerequisites

- Android device running Android 5.0 (API level 21) or above.
- Android Studio version 4.0 or above.

## Getting Started

1. Clone the repository: https://github.com/devsaini1511/SmitchTask.git

2. Open the project in Android Studio.

3. Build and run the app on your Android device.

## Usage

1. Launch the app on your Android device.

2. **Publish mDNS Service**
   - Enter the service details (Instance Name, Service Type, Domain, Port) in the input fields.
   - Tap the "Publish" button to publish the mDNS service.

3. **Scan mDNS Services**
   - Tap the "Scan" button to start scanning for available mDNS services.
   - The scan results will be displayed in a list, showing the service name, type, IP address, and port.
   - The list will update automatically when new services are discovered or existing services are lost.

4. **Find BLE Devices**
   - Tap the "Find BLE" button to start scanning for nearby BLE devices.
   - The BLE devices found will be displayed in a list.
   - Tap on a device in the list to initiate the pairing process.

## Architecture and Libraries

The app follows the MVVM (Model-View-ViewModel) architecture pattern and uses the following libraries:

- Kotlin programming language
- Android Architecture Components (ViewModel, LiveData)
- Dagger Hilt for dependency injection
- RecyclerView for displaying lists
- Android Networking (mDNS)
- Bluetooth Low Energy (BLE)

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.


