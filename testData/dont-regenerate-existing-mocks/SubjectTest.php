<?php

namespace App\Tests;

use App\SubjectUnderTest;
use Prophecy\Prophecy\ObjectProphecy;
use Psr\Log\LoggerInterface;

class SubjectTest
{
    /** @var LoggerInterface|ObjectProphecy */
    private $logger;
    /** @var int */
    private $i;
    /** @var SubjectUnderTest */
    private $subject;

    public function setUp()
    {
        parent::setUp();
        $this->logger = $this->prophesize(LoggerInterface::class);
        $this->i = 0;

        $this->subject = new SubjectUnderTest<caret>($this->logger->reveal(), $this->i);
    }
}
