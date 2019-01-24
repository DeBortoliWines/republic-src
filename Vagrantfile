Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/trusty64"
  config.vm.provider "trusty64" do |v|
      v.memory = 2048
      v.cpus = 2
  end

  config.ssh.forward_agent = true
  config.ssh.forward_x11 = true

  config.vm.provision "ansible" do |ansible|
    ansible.galaxy_role_file = "Vagrantfile-ansible-requirements.yml"
    ansible.galaxy_roles_path = ".ansible-galaxy-roles"
    ansible.playbook = "Vagrantfile-playbook.yml"
  end
end
