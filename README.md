# Quick Mock
[![Build Status](https://travis-ci.com/cawolf/phpstorm-quick-mock.svg?branch=master)](https://travis-ci.com/cawolf/phpstorm-quick-mock)

PHPStorm plugin allowing you to quickly create mock objects from within a test class

When writing tests for classes with constructor dependencies (e.g. every composed service in a DI framework like
[Symfony](https://symfony.com/)), it is a tedious job to mock these dependencies. While "Live Templates" and "File 
Templates" can help with the test case skeleton, the actual class dependencies have to be mocked manually.

Now, you can use this PHPStorm plugin to automatically generate mocks for these dependencies. Place your cursor in the empty
constructor argument list, trigger code intentions (default: `ALT + ENTER`) and select `Create mocks for all parameters 
of the constructor` - done!

## Install
Install the plugin by going to `Settings -> Plugins -> Browse repositories` and then search for `Quick Mock`

## Issues
Have a look at https://github.com/cawolf/phpstorm-quick-mock/issues and add your comment or open a new issue.
