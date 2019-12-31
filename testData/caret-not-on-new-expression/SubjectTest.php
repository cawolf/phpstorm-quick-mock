<?php

namespace App\Tests;

use App\SubjectUnderTest;

class SubjectTest
{
    /** @var SubjectUnderTest */
    private $subject;

    public function setUp()
    {
        parent::setUp();
        $this->subject<caret> = new SubjectUnderTest();
    }
}
