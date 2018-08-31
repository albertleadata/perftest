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

def launchStandalone() {
	sh './bin/bjtst standalone'
	return
}

def launchPerfTest() {
	sh './bin/bjtst controller'
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
		"controller003" : { launchPerfTest() }
	)
}

pipeline {
//	agent any
	agent { label "bluejay" }
	stages {
		stage('test') {
		//	steps { launchStandalone() } post { always { publishViaSSH() } }
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
//					agent { label "controller003" }
//					steps { launchPerfTest() }
//					post { always { publishViaSSH() } }
//				}
//			}
			steps { sh 'echo "Testing suspended by configuration"' }
		}
	}
}
