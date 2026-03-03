# Dual-Write Reseed

## Overview
A Kotlin-based Android utility that synchronizes files between internal and external storage. Designed as a barrier against persistent deletion, the app ensures that critical files remain available even if one storage location is compromised (e.g., via Safe Mode access to the file manager).

## Core Workflow
1. **Initial Setup**
   - User specifies file locations for both **internal** and **external** storage.

2. **Synchronization**
   - On tapping **Sync**, the app compares both storage locations.
   - Any file present in one location but missing in the other is automatically copied over.
   - Result: both storages contain the same set of files.

3. **Persistence Layer**
   - Provides redundancy so that files remain accessible even if one storage is tampered with.
   - Acts as a **supporting barrier** to Application Lock, which secures access to file manager apps.

## Tech Stack
- **Language**: Kotlin
- **Platform**: Android SDK
- **System Features**:
  - File I/O operations
  - Internal and external storage handling
  - Synchronization logic

## Features
- Configure file location in internal storage.
- Configure file location in external storage.
- Synchronize data between internal and external storage.

## Impact
- Ensures redundancy by cloning data across storage locations.
- Preserves client data against accidental or intentional deletion.
- Adds layered security for critical files.

## Project Structure
- `/ui` – User interface for selecting storage paths and triggering sync
- `/logic` – Synchronization engine (file comparison and copy operations)
- `/data` – Storage handlers for internal and external paths

## Setup Instructions
1. Install the application on the device.
2. On first launch, set file locations for internal and external storage.
3. Tap **Sync** to initiate synchronization.
4. Files will be mirrored across both storage locations.

## Notes
- This project is part of my mobile security simulation portfolio.
- Designed as a **data integrity layer** to complement Application Lock.
