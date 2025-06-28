{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
    rust-overlay = {
      url = "github:oxalica/rust-overlay";
      inputs = {
        nixpkgs.follows = "nixpkgs";
      };
    };
  };

  outputs = { self, nixpkgs, flake-utils, rust-overlay }:
    flake-utils.lib.eachDefaultSystem
      (system:
        let
          overlays = [ (import rust-overlay) ];

          pkgs = import nixpkgs {
            inherit system overlays;
            config.allowUnfree = true;
            config.android_sdk.accept_license = true;
          };

          rustToolchain = pkgs.rust-bin.stable.latest.default.override {
            extensions = [ "rust-src" ];
            targets = [
              "aarch64-linux-android"
              "armv7-linux-androideabi"
              "i686-linux-android"
              "x86_64-linux-android"
            ];
          };

          # TODO: for cargo-ndk for compiling. can android studio share the sdk install somehow?
          # androidComposition = pkgs.androidenv.composeAndroidPackages {
          #   buildToolsVersions = [
          #     "34.0.0"
          #   ];
          #   platformVersions = [
          #     "33"
          #     "34"
          #     "35"
          #     "latest"
          #   ];
          #   includeNDK = true;
          # };
          # lastBuildTools = pkgs.lib.lists.last androidComposition.build-tools;

          nativeBuildInputs = [
            rustToolchain
            pkgs.just
          ];
          buildInputs = [
            # pkgs.jdk
            pkgs.cargo-ndk
          ];
        in
        {
          devShells.default = pkgs.mkShell rec {
            inherit nativeBuildInputs buildInputs;

            # JAVA_HOME = "${pkgs.jdk}";

            # ANDROID_HOME = "${androidComposition.androidsdk}/libexec/android-sdk";
            # GRADLE_OPTS = "-Dorg.gradle.project.android.aapt2FromMavenOverride=${ANDROID_HOME}/build-tools/${lastBuildTools.version}/aapt2";
          };
        }
      );
}
