# v1.3.3
* resolve constructor properly if the class itself does now own a constructor (#35)

# v1.3.2
* fix mock generation for union types (#30)
* fix regression: existing mocks are not regenerated anymore (#29)
* fix type hints for `mixed` properties (#28)

# v1.3.1
* colliding inherited properties are now handled (#26)

# v1.3.0
* aliased imports are now prophesized correctly (#22)

# v1.2.1
* updated meta information to keep plugin enabled in newer PHPStorm versions

# v1.2.0
* do not regenerate mocks if already present (#18)
* added configuration for addition of doc blocks (#15)
* also add primitives as members of the test case (#14)
* provide no intention for classes without constructor (#13)

# v1.1.0
* added ObjectProphecy doc (#8)
* show intention if there are whitespaces between parenthesis (#7)
* show intention not only between parenthesis (#6)
* added whitespace between mocks and subject creation (#5)
* do not mock primitives anymore (#4)

# v1.0.2
* added "->reveal()" to constructor arguments (#1)

# v1.0.1
* added gradle publishing support

# v1.0
* initial release
