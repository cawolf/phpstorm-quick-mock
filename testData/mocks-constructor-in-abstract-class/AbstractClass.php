<?php

namespace App;

abstract class AbstractClass
{
    /**
     * @var string
     */
    private $someString;

    public function __construct(string $someString) {
        $this->someString = $someString;
    }
}