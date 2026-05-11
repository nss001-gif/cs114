# CS Lab: Mandelbrot Set — Project README

## Project structure

```
MandelbrotLab/
├── lib/
│   └── junit-platform-console-standalone.jar   (do not modify)
├── src/
│   ├── main/java/mandelbrot/
│   │   └── MandelbrotLab.java                  ← YOUR WORK GOES HERE
│   └── test/java/mandelbrot/
│       └── MandelbrotLabTest.java              (do not modify)
└── README.md
```

## Step 1 — Compile

Run this command from the MandelbrotLab/ folder:

**Mac / Linux:**
```
javac -cp lib/junit-platform-console-standalone.jar \
      -d out \
      src/main/java/mandelbrot/MandelbrotLab.java \
      src/test/java/mandelbrot/MandelbrotLabTest.java
```

**Windows (Command Prompt):**
```
javac -cp lib\junit-platform-console-standalone.jar -d out src\main\java\mandelbrot\MandelbrotLab.java src\test\java\mandelbrot\MandelbrotLabTest.java
```

## Step 2 — Run the tests

**Mac / Linux:**
```
java -jar lib/junit-platform-console-standalone.jar \
     --class-path out \
     --select-class mandelbrot.MandelbrotLabTest
```

**Windows:**
```
java -jar lib\junit-platform-console-standalone.jar --class-path out --select-class mandelbrot.MandelbrotLabTest
```

Aim for all tests to pass before submitting.

## Step 3 — Render the image

```
java -cp out mandelbrot.MandelbrotLab
```

This creates **mandelbrot.png** in the current folder. Open it to see your fractal.

## Submission

Submit only: `src/main/java/mandelbrot/MandelbrotLab.java`
