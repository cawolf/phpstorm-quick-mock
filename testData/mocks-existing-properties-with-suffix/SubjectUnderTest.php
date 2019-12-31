<?php

namespace App;

class SubjectUnderTest
{
    private $data;

    public function __construct(string $data)
    {
        $this->data = $data;
    }
}