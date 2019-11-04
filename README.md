# JCodeFlattener [![](https://jitpack.io/v/YoshikiHigo/JCodeFlattener.svg)](https://jitpack.io/#YoshikiHigo/JCodeFlattener)

JCodeFlatter is a program that convert Java source code to flattened one.
In flattened code, all complex program statement are decomposed to simple ones.

# Concept
The concept of JCodeFlatter is that flattened code is compilable and flattened code has the same behavior as its original code.
However, currently, JCodeFlattener occasionally yields uncompilable code due to failing to resolving name bindings.

# Command Line Options
JCodeFlatter is a command line tool and it can take the following options.
- "-i" ("--input") specifies a target Java source file or a target directory including target Java source files.
- "-o" ("--output") specifies an output file name or an output directory name.
- "-l" ("--library") specifies jar files that are used for resolving name bindings in the target Java source files. In Windows environment, use ";" to specify multiple jars. In Mac/Linux environment, use ":".
- "-r" ("--rapid") specifies not to attempt name resolving. Using this option makes execution much faster.
- "-v" ("--verbose") specifies verbose progress output.
- "-q" ("--quiet") specifies not to output any progress during execution. This option can't be specified with "-v".
- "-a" ("--aggressive") specifies to flatten code more aggressively.
- "-t" ("--threads") specifies the number of threads for flattening code. The default value is "1".
- "-n" ("--dry-run") specifies not to flatten code.
- "-f" ("--format") specifies to reformat source code.
- "-c" ("--comment") specifies to remove code comments.
