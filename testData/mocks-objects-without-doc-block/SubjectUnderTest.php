<?php

namespace App;

use Psr\Log\LoggerInterface;

class SubjectUnderTest
{
    private $logger;

    public function __construct(LoggerInterface $logger)
    {
        $this->logger = $logger;
    }
}