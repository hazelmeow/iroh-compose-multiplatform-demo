# Iroh Compose Multiplatform Demo

This is a demo of a cross-platform app with shared core logic using Rust and [Iroh](https://www.iroh.computer/) and shared UI using [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/).
The UI and Rust logic communicate using [UniFFI](https://mozilla.github.io/uniffi-rs/latest/), with bindings for Kotlin Multiplatform handled by [Gobley](https://gobley.dev/).

Android and desktop work, I haven't tested with iOS yet.

I've been using Android Studio for building the Android app, and IDEA for building the desktop app.
You will also need a Rust toolchain with additional targets installled for the targets you're building for (the Android and iOS targets).
Gradle/Gobley are currently configured to only build desktop for the host target.

NixOS: I'm letting Android Studio/IDEA manage the JDK and Android SDK.
The flake in `nix/` installs a Rust toolchain since I don't have one installed globally.
I launch `android-studio .` or `idea-community .` inside the repository root so that `cargo` is in the path.
I also had to disable some tasks from Gobley which call `rustup` to install required targets, but my install doesn't use rustup.

There is also a Cargo example (`cargo run --example headless`) that starts a node running the same echo protocol as the app for testing.
