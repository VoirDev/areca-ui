# Areca Playground

The playground is a JVM desktop application containing interactive examples for the Areca modules.

Run it from the repository root:

```shell
./gradlew :playground:run
```

The sidebar groups examples by component area and lets you switch between the available showcases.
Flows open as folders: choosing one shows its screens and parts in place of the top-level list.

## Documentation images

The screenshots under `docs/images/` are rendered from this module, headless, in light and dark:

```shell
./gradlew :playground:renderDocImages
./gradlew :playground:renderDocImages -Pshots=charts,datetime
```

Each image is a shot in `src/jvmMain/kotlin/dev/voir/areca/playground/docs/`, built from the
published components with the same demo content as the showcases. Re-run the task when a
component's look changes and commit the PNGs. It is not part of `check`: text rasterises
differently from one machine to the next.
