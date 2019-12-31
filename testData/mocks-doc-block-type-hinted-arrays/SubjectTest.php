<?php

namespace App\Tests;

use SubjectUnderTest;

class SubjectTest
{
    /** @var SubjectUnderTest */
    private $subject;

    protected function setUp(): void
    {
        $this->subject = new SubjectUnderTest<caret>();
    }
}