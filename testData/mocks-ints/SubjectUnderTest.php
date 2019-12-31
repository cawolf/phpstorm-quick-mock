<?php

namespace App;

class SubjectUnderTest
{
    private $data;

    public function __construct(int $data)
    {
        $this->data = $data;
    }
}