# Rules

- Do not modify the file GoldenTilesColors.kt, and do not use any of the values defines there.
- Use functions from the androidx.wear.protolayout.material3 package where possible. It is very
  unlikely that you should be generating any code that uses the androidx.wear.protolayout.material
  library; please flag this if that appears to be required.
- After making a change to a file, run the build-apk tool to see if there are any errors, and
  resolve any that appear.
- Use LayoutModifier.* functions instead of Modifiers.Builder().* functions. Use
  .toProtoLayoutModifiers() if you need to convert it to the "old" type.

# Useful Resources

The full source code to the tiles and protolayout libraries is available in the files
samples/tiles.txt and samples/proto.txt, which you may consult for your
reference.

Also in this directory is some sample code to produce layouts. This code is missing the support
infrastructure (i.e. the service to create the layouts, etc.), but the layout function calls are
syntactically correct. You may wish to consult the sample/*.kt files in this directory when dealing
with layout issues.  
