# Quick Mock
[![Tests](https://github.com/cawolf/phpstorm-quick-mock/actions/workflows/test.yml/badge.svg)](https://github.com/cawolf/phpstorm-quick-mock/actions/workflows/test.yml)
[![Version](https://img.shields.io/jetbrains/plugin/v/11165?label=version)](https://plugins.jetbrains.com/plugin/11165-quick-mock)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/11165)](https://plugins.jetbrains.com/plugin/11165-quick-mock)

PHPStorm plugin allowing you to quickly create mock objects from within a test class

When writing tests for classes with constructor dependencies (e.g. every composed service in a DI framework like
[Symfony](https://symfony.com/)), it is a tedious job to mock these dependencies. While "Live Templates" and "File 
Templates" can help with the test case skeleton, the actual class dependencies have to be mocked manually.

Now, you can use this PHPStorm plugin to automatically generate mocks for these dependencies. Place your cursor in the empty
constructor argument list, trigger code intentions (default: `ALT + ENTER`) and select `Quick Mock: add constructor prophecies` - done!

## Install
Install the plugin by going to `Settings -> Plugins -> Marketplace` and then search for `Quick Mock`

## Options
You can configure the plugin under `Languages and Frameworks -> PHP -> Quick Mock`. Available options:

* add DocBlock for generated members: if checked, a DocBlock will be generated for generated mock members

## Usage
Let's assume you have this business service:


    use Psr\Log\LoggerInterface;
    use Symfony\Component\Routing\RouterInterface;

    class Subject
    {
        /** @var LoggerInterface */
        private $logger;
        /** @var RouterInterface */
        private $router;
    
        public function __construct(LoggerInterface $logger, RouterInterface $router)
        {
            $this->logger = $logger;
            $this->router = $router;
        }
        
        public function doBusinessLogic(string $p1, int $p2, float $p3): string
        {
            [...]
        }
    }
    
Whenever you begin writing a unit test case, you will inevitably come to the subject construction:


    use Service\Subject;

    class SubjectTest extends TestCase
    {
        /** @var Subject */
        private $subject;
        
        public function setUp()
        {
            $this->subject = new Subject();
        }
        
        public function testBusinessLogic()
        {
            self::assertEquals('expected result', $this->subject->doBusinessLogic('parameter 1', 2, 3.0));
        }
    }

Place your cursor anywhere at the `new` expression of a subject under test, trigger code intentions (default: 
`ALT + ENTER`) and select `Quick Mock: add constructor prophecies` - done!


    use Psr\Log\LoggerInterface;
    use Service\Subject;
    use Symfony\Component\Routing\RouterInterface;

    class SubjectTest extends TestCase
    {
        /** @var LoggerInterface */
        private $logger;
        /** @var RouterInterface */
        private $router;
        /** @var Subject */
        private $subject;
    
        public function setUp()
        {
            $this->logger = $this->prophesize(LoggerInterface::class);
            $this->router = $this->prophesize(RouterInterface::class);
            $this->subject = new Subject($this->logger->reveal(), $this->router->reveal());
        }
        
        public function testBusinessLogic()
        {
            self::assertEquals('expected result', $this->subject->doBusinessLogic('parameter 1', 2, 3.0));
        }
    }

## Issues
Have a look at https://github.com/cawolf/phpstorm-quick-mock/issues and add your comment or open a new issue.
