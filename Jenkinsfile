def deployViaSSH( war, id) {
	def sDHN = 'wwwlogin@wwwhost'
	def sDPS = '/path/to/j2ee/deployment/location'
	def sTgt = "${sDHN}:${sDPS}"
	sh "scp ${war} ${sTgt}/${id}.war"
}

def publishViaSSH() {
	def iBld = "${env.BUILD_ID}"
	sh "echo \"${iBld}\" > jobid.txt"
	sh './bin/bjtst publish'
}

def launchPerfTest() {
	sh './bin/bjtst controller'
	publishViaSSH()
	return
}

def launchLoadGen() {
	sh './bin/bjtst loadgen'
	return
}

def launchParallelPerfTest() {
	parallel (
		"loadgen002" : { launchLoadGen() },
		"loadgen001" : { launchLoadGen() },
		"controller" : { launchPerfTest() }
	)
}

pipeline {
//	agent any
	agent { label "Jenkins" }
	stages {
		stage('test') {
//			parallel {
//				stage('Load Gen 2') {
//					agent { label "loadgen002" }
//					steps { launchLoadGen() }
//				}
//				stage('Load Gen 1') {
//					agent { label "loadgen001" }
//					steps { launchLoadGen() }
//				}
//				stage('Load Controller') {
//					agent { label "controller" }
//					steps { launchPerfTest() }
//				}
//			}
			steps { sh 'echo "Testing suspended by configuration"' }
		}
	}
}
