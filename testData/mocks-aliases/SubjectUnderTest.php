<?php

namespace App;

use Psr\Log\LoggerInterface;
use App\LoggerInterface as MainLoggerInterface;

class SubjectUnderTest
{
    private $logger;
    private $mainLogger;

    public function __construct(LoggerInterface $logger, MainLoggerInterface $mainLogger)
    {
        $this->logger = $logger;
        $this->mainLogger = $mainLogger;
    }
}