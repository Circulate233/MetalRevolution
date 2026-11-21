
plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

minecraft {
    extraRunJvmArguments.add("-noverify")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    implementation("org.ow2.asm:asm-commons:9.2")
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("it.unimi.dsi:fastutil:8.5.18")
}
