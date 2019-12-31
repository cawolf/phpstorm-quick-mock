<?php

namespace App;

class SubjectUnderTest
{
    private $data;

    public function __construct(bool $data)
    {
        $this->data = $data;
    }
}