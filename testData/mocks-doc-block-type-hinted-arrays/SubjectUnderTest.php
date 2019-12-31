<?php

use App\Gitlab\Model\GitlabMapping;
use Http\Client\HttpClient;
use JMS\Serializer\SerializerInterface;

class SubjectUnderTest
{
    private $gitlabClient;
    private $serializer;
    private $mapper;
    private $jiraConfig;

    /**
     * @param string[] $jiraConfig
     */
    public function __construct(HttpClient $gitlabClient, SerializerInterface $serializer, GitlabMapping $mapper, array $jiraConfig)
    {
        $this->gitlabClient = $gitlabClient;
        $this->serializer = $serializer;
        $this->mapper = $mapper;
        $this->jiraConfig = $jiraConfig;
    }
}
