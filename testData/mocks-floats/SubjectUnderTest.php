<?php

namespace App;

class SubjectUnderTest
{
    private $data;

    public function __construct(float $data)
    {
        $this->data = $data;
    }
}