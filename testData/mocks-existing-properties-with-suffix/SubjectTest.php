<?php

namespace App\Tests;

use App\SubjectUnderTest;
use App\TestCase;

class SubjectTest extends TestCase
{
    /** @var SubjectUnderTest */
    private $subject;

    public function setUp()
    {
        parent::setUp();
        $this->subject = new SubjectUnderTest<caret>();
    }
}
