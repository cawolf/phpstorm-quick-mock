<?php

namespace App;

use Psr\Log\LoggerInterface;

class SubjectUnderTest
{
    private $logger;
    private $i;
    private $s;

    public function __construct(LoggerInterface $logger, int $i, string $s)
    {
        $this->logger = $logger;
        $this->i = $i;
        $this->s = $s;
    }
}